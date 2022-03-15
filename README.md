# sam-app-with-layers

This projects shows up SAM issue related to publishing SAM application into Serverless Application Repository.  I followed the instructions found in [AWS Serverless Application Model Developers Guide Publishing](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-publishing-applications.html).  If I build and deploy this project
using SAM cli then publish it to AWS Serverless Application Repository the code artifacts won't be existing in the SAR application.  You can
deploy that application via any means it will not work properly since it doesn't have the appropriate code assets.

## SAM CLI version
```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam --version
SAM CLI, version 1.40.1

```

## Commands to recreate issue:
```shell
export SAM_CLI_BETA_MAVEN_SCOPE_AND_LAYER=1 
sam build
sam deploy --guided
sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket tra-sam-deployment --s3-prefix sam-app-with-layers
sam publish --template packaged.yaml --region us-east-2
```

#### sam publish output
```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam publish --template packaged.yaml --region us-east-2
Publish Succeeded
Created new application with the following metadata:
{
  "Name": "SamAppWithLayers",
  "Author": "Phillip Rower",
  "Description": "Demonstrates and documents using SAM with Serverless Application Repository",
  "ReadmeUrl": "s3://tra-sam-deployment/sam-app-with-layers/ded2bfd58767e0470bdfcfa78ddf9607",
  "SemanticVersion": "1.0.0",
  "SourceCodeUrl": "https://github.com/prower-turnitin/sam-app-with-layers"
}
Click the link below to view your application in AWS console:
https://console.aws.amazon.com/serverlessrepo/home?region=us-east-2#/published-applications/arn:aws:serverlessrepo:us-east-2:431036401867:applications~SamAppWithLayers
phillipr@es-admins-MBP-2 sam-app-with-layers % 

```

The application deployed using `sam deploy --guided` works fine.  Let's deploy it from AWS SAR using command line as documented here in
[AWS Serverless Application Repository Developer Guide](https://docs.aws.amazon.com/serverlessrepo/latest/devguide/serverlessrepo-how-to-consume.html).

1. Create CloudFormation Change Set using SAR Application Id
```shell
aws serverlessrepo create-cloud-formation-change-set \
--application-id arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers \
--stack-name deploy-sam-app-with-layers-from-sar \
--capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND
```
Output:
```json
{
    "ApplicationId": "arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers",
    "ChangeSetId": "arn:aws:cloudformation:us-east-2:431036401867:changeSet/ae41b03c5-0d68-4722-be67-71b408ac2f2f/b7d91c36-683a-4a17-a2a5-e006cc5f5646",
    "SemanticVersion": "1.0.0",
    "StackId": "arn:aws:cloudformation:us-east-2:431036401867:stack/serverlessrepo-deploy-sam-app-with-layers-from-sar/fe089030-a491-11ec-a3a4-0281bc888cfe"
}

```

2. Execute that change set to allocate AWS resources:
```shell
aws cloudformation execute-change-set \
--change-set-name arn:aws:cloudformation:us-east-2:431036401867:changeSet/ae41b03c5-0d68-4722-be67-71b408ac2f2f/b7d91c36-683a-4a17-a2a5-e006cc5f5646
```

3. Get Stack Details verify deployment
```shell
aws cloudformation describe-change-set --change-set-name arn:aws:cloudformation:us-east-2:431036401867:changeSet/ae41b03c5-0d68-4722-be67-71b408ac2f2f/b7d91c36-683a-4a17-a2a5-e006cc5f5646


```

Get stack name from output.

4. Find and display stack details and find HelloWorldApi output URL.
```shell
aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE
aws cloudformation describe-stacks --stack-name serverlessrepo-deploy-sam-app-with-layers-from-sar
```

5. Hit that URL in which you will receive an Internal server error. Why when deploying from `sam deploy --guided` works fine?
It seems SAM cli doesn't resolve all endpoints same when using `aws package` versus `aws deploy --guided`.
6. Find lambda function arn and get lambda details.
```shell
aws lambda get-function --function-name arn:aws:lambda:us-east-2:431036401867:function:serverlessrepo-deploy-sam-app-w-HelloWorldFunction-6TwuMKPXfNXM
```
7. Download the code:
```shell
curl $(aws --no-cli-pager lambda get-function --function-name arn:aws:lambda:us-east-2:431036401867:function:serverlessrepo-deploy-sam-app-w-HelloWorldFunction-6TwuMKPXfNXM | jq -r .Code.Location) --output deploy-sar-lambda-code.zip

```
8. Unzip contents and notice they are just the source files.
9. Let's get the contents of code from earlier direct deploy of the stack using `sam deploy --guided`.
```shell
aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE
aws cloudformation describe-stacks --stack-name sam-app-with-layers
```
10. Use the lambda arn to display details and download its source.
```shell
aws lambda get-function --function-name arn:aws:lambda:us-east-2:431036401867:function:sam-app-with-layers-HelloWorldFunction-1bSIXt6MiS4V
curl $(aws --no-cli-pager lambda get-function --function-name arn:aws:lambda:us-east-2:431036401867:function:sam-app-with-layers-HelloWorldFunction-1bSIXt6MiS4V | jq -r .Code.Location) --output deploy-sam-guided-code.zip
```
11. Unzip deploy-sam-guided-code.zip contents and notice it contains compiled java class files.

Gist of this the same SAM project template.yaml file which works fine with `sam deploy --guided` doesn't work with
`sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket tra-sam-deployment --s3-prefix sam-app-with-layers`.
The `sam deploy --guided` is packaging up all the appropriate code assets in the appropriate format uploading to S3 along with a
resolved template.  You can find the source and SAM template in S3 by examining contents of `samconfig.toml` created by SAM cli.

12. Locate resolved template using bucket and prefix in `samconfig.toml`;

The most recent `*.template` file is usually one you want.

```shell
aws --no-cli-pager s3api list-objects-v2 --bucket aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j --prefix sam-app-with-layers 
aws s3api get-object --bucket aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j --key sam-app-with-layers/f039bd53d1746ed2e487989b1460f772.template publish.yaml
```

13. Work around to publishing SAM application correctly would be to use this `publish.yaml` with `sam publish`, but first remove the corrupt
publication version.

```shell
sam publish --template publish.yaml --region us-east-2
```

## List of issues found while trying to find a work around:
I found few different issues when attempting to publish this project to SAR.  

1. `sam deploy --guided --s3-bucket tra-sam-deployment --s3-prefix sam-app-with-layers` will not use my bucket or prefix:

```shell
sam build
sam deploy --guided --s3-bucket tra-sam-deployment --s3-prefix my-sam-app-with-layers
``` 

[Console Log for sam deploy](samDeployLogs.md)

The above `sam deploy` won't use my bucket.  Below is contents of default samconfig.toml created by the above `sam deploy --guided --s3-bucket tra-sam-deployment --s3-prefix my-sam-app-with-layers`.  Notice how it doesn't use my bucket or prefix.

#### samconfig.toml
```toml
version = 0.1
[default]
[default.deploy]
[default.deploy.parameters]
stack_name = "sam-app-with-layers"
s3_bucket = "aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j"
s3_prefix = "sam-app-with-layers"
region = "us-east-2"
confirm_changeset = true
capabilities = "CAPABILITY_IAM"
image_repositories = []


```

Ok, lets list the object contents of that s3 bucket and path.  There is nothing there.

```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % aws s3 ls s3://tra-sam-deployment/my-sam-app-with-layers
phillipr@es-admins-MBP-2 sam-app-with-layers % 

```

2. I tried building my own samconfig.toml and passing it during the command line to use my s3 bucket and path.

#### [mysamconfig.toml](mysamconfig.toml)
```toml
version = 0.1
[default]
[default.deploy]
[default.deploy.parameters]
stack_name = "sam-app-with-layers"
s3_bucket = "tra-sam-deployment"
s3_prefix = "my-sam-app-with-layers"
region = "us-east-2"
confirm_changeset = true
capabilities = "CAPABILITY_IAM"
image_repositories = []
```

```shell
sam build
sam deploy --guided --config-file mysamconfig.toml
```

[Console Logs](samDeployLogs2.md)

After running this command it updates mysamconfig.toml changing bucket to the default.

#### mysamconfig.toml after running "sam deploy --guided --config-file mysamconfig.toml"
```toml
version = 0.1
[default]
[default.deploy]
[default.deploy.parameters]
stack_name = "sam-app-with-layers"
s3_bucket = "aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j"
s3_prefix = "sam-app-with-layers"
region = "us-east-2"
confirm_changeset = true
capabilities = "CAPABILITY_IAM"
image_repositories = []
```

Ok, lets list the object contents of that s3 bucket and path.  There is nothing there again.

```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % aws s3 ls s3://tra-sam-deployment/my-sam-app-with-layers
phillipr@es-admins-MBP-2 sam-app-with-layers % 

````

Still can't get it to use my bucket.  

3. I told it to save results of any config changes which I made none but I'll try again answering No for that question this time.

```shell
sam build
sam deploy --guided --config-file mysamconfig.toml
```

[Console Logs](samDeployLogs2.md)

Answering No on saving config file does as expected and doesn't change my `mysamconfig.toml` file.  
Unfortunately it still does use my bucket or path contained in `mysamconfig.toml` and still uses the default.

```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % aws s3 ls s3://tra-sam-deployment/my-sam-app-with-layers
phillipr@es-admins-MBP-2 sam-app-with-layers % 

```


## How to fix it?

1. `sam build SampleLayer` to build layer first and make it available on local maven repository

1. `sam build` to build others project resources

