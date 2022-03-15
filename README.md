# sam-app-with-layers

This projects shows up SAM issue related to publishing SAM application into Serverless Application Repository.  I found a
few different issues when attempting to publish this project to SAR.  One issue I found is overriding s3 bucket and prefix via
command line parameters doesn't work. Overriding which 

## SAM CLI version
```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam --version
SAM CLI, version 1.40.1

```

## List of issues found:

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

