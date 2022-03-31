# Issue Publishing this app sam-app-with-layers to AWS Serverless Application Repository

This projects shows up SAM issue related to publishing SAM application into Serverless Application Repository.  I followed the instructions found in [AWS Serverless Application Model Developers Guide Publishing](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-template-publishing-applications.html).  If I build and deploy this project
using SAM cli then publish it to AWS Serverless Application Repository the code artifacts won't be existing in the SAR application.  You can
deploy that application via any means it will not work properly since it doesn't have the appropriate code assets.

Gist of the reason for this is same `template.yaml` used by `sam deploy --guided` can't be used successfully by `sam package`.
In this example SAM project Maven pom.xml files aren't configuring the deployment artifacts instead `sam deploy --guided` is building these artifacts.
One could use Maven Assembly Plugin to build the appropriate zip file, but why should both SAM cli and Maven do this packaging.
Notice the `template.yaml` ContentUri wouldn't be the same if you tried to use both SAM and Maven to package the assets.  
Features like `sam sync` and `sam deploy --guided` would need different configuration in `template.yaml` than required for a
`template.yaml` where Maven or Gradle packaged the Java assets.  Personally it makes sense to me for Java SAM projects to just use 
SAM cli to do the packaging.  
One option to resolve this would be to add an option feature to `sam build --guided` like --output-template-file to get the resolved
version of the SAM template.yaml.  
Another would be to keep copy of last resolved template in the hidden `.aws-sam` folder, which could be used with the 
`sam publish` command.  I also found list of issue when trying to discover a solution to this issue such as the
s3 bucket override doesn't work too with `sam deploy --guided` command.

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
aws serverlessrepo delete-application --application-id arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers
sam publish --template publish.yaml --region us-east-2
```

#### Output Publish
```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam publish --template publish.yaml --region us-east-2
Publish Succeeded
Created new application with the following metadata:
{
  "Name": "SamAppWithLayers",
  "Author": "Phillip Rower",
  "Description": "Demonstrates and documents using SAM with Serverless Application Repository",
  "ReadmeUrl": "s3://aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j/sam-app-with-layers/ded2bfd58767e0470bdfcfa78ddf9607",
  "SemanticVersion": "1.0.0",
  "SourceCodeUrl": "https://github.com/prower-turnitin/sam-app-with-layers"
}
Click the link below to view your application in AWS console:
https://console.aws.amazon.com/serverlessrepo/home?region=us-east-2#/published-applications/arn:aws:serverlessrepo:us-east-2:431036401867:applications~SamAppWithLayers

```

14. Repeat deployment steps starting at 1 to deploy application from Serverless Application Repository this time it will work, but 
first delete the old corrupted stack from the bad publish.
```shell
aws cloudformation delete-stack --stack-name deploy-sam-app-with-layers-from-sar
aws serverlessrepo create-cloud-formation-change-set \
--application-id arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers \
--stack-name deploy-sam-app-with-layers-from-sar \
--capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND
aws cloudformation execute-change-set \
--change-set-name arn:aws:cloudformation:us-east-2:431036401867:changeSet/a0e6c1817-f868-47a2-ba18-7ec511c52c03/51bbaef7-9642-4ca0-95e0-f6c3f8d82f34
aws cloudformation describe-change-set --change-set-name arn:aws:cloudformation:us-east-2:431036401867:changeSet/a0e6c1817-f868-47a2-ba18-7ec511c52c03/51bbaef7-9642-4ca0-95e0-f6c3f8d82f34
aws cloudformation describe-stacks --stack-name serverlessrepo-deploy-sam-app-with-layers-from-sar
```

Hit stack URL found in Output HelloWorldApi and you should get successful 200 result.


## List of other issues found while trying to find a work around:
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


### Alternative instructions
```shell
export SAM_CLI_BETA_MAVEN_SCOPE_AND_LAYER=1 
sam build
sam package --output-template-file packaged.yaml --s3-bucket tra-sam-deployment --s3-prefix sam-app-with-layers
sam publish --template packaged.yaml --region us-east-2
```

```shell
aws serverlessrepo create-cloud-formation-change-set \
--application-id arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers \
--stack-name deploy-sam-app-with-layers-from-sar-2 \
--capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND
```

#### result
```json
{
    "ApplicationId": "arn:aws:serverlessrepo:us-east-2:431036401867:applications/SamAppWithLayers",
    "ChangeSetId": "arn:aws:cloudformation:us-east-2:431036401867:changeSet/a5cb54392-108f-4e1a-bc91-61f8ee091184/5dae47b2-b561-4cf9-840a-a7b0e2a6eca2",
    "SemanticVersion": "1.0.1",
    "StackId": "arn:aws:cloudformation:us-east-2:431036401867:stack/serverlessrepo-deploy-sam-app-with-layers-from-sar-2/a22c13f0-b115-11ec-a6fc-0a6638343364"
}

```


```shell
aws cloudformation execute-change-set \
--change-set-name arn:aws:cloudformation:us-east-2:431036401867:changeSet/a5cb54392-108f-4e1a-bc91-61f8ee091184/5dae47b2-b561-4cf9-840a-a7b0e2a6eca2
```

#### Display and grab artifacts
```shell
aws s3 ls s3://tra-sam-deployment/sam-app-with-layers/7c0ad793a397a526b45a92a615808a16
aws --no-cli-pager s3api list-objects-v2 --bucket tra-sam-deployment --prefix sam-app-with-layers
aws --no-cli-pager s3api list-objects-v2 --bucket tra-sam-deployment --prefix sam-app-with-layers/7c0ad793a397a526b45a92a615808a16
aws s3api get-object --bucket tra-sam-deployment --key sam-app-with-layers/7c0ad793a397a526b45a92a615808a16 helloWorld.zip
s3://tra-sam-deployment/sam-app-with-layers/7c0ad793a397a526b45a92a615808a16
```