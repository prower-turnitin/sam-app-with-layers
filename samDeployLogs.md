# "sam deploy --guided" logs
```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam deploy --guided --s3-bucket tra-sam-deployment --s3-prefix my-sam-app-with-layers

Configuring SAM deploy
======================

        Looking for config file [samconfig.toml] :  Not found

        Setting default arguments for 'sam deploy'
        =========================================
        Stack Name [sam-app]: sam-app-with-layers
        AWS Region [us-east-2]: 
        #Shows you resources changes to be deployed and require a 'Y' to initiate deploy
        Confirm changes before deploy [y/N]: y
        #SAM needs permission to be able to create roles to connect to the resources in your template
        Allow SAM CLI IAM role creation [Y/n]: y
        #Preserves the state of previously provisioned resources when an operation fails
        Disable rollback [y/N]: 
        HelloWorldFunction may not have authorization defined, Is this okay? [y/N]: y
        Save arguments to configuration file [Y/n]: y
        SAM configuration file [samconfig.toml]: 
        SAM configuration environment [default]: 

        Looking for resources needed for deployment:
         Managed S3 bucket: aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j
         A different default S3 bucket can be set in samconfig.toml

        Saved arguments to config file
        Running 'sam deploy' for future deployments will use the parameters saved above.
        The above parameters can be changed by modifying samconfig.toml
        Learn more about samconfig.toml syntax at 
        https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-config.html

File with same data already exists at sam-app-with-layers/386184f385f736c179d0e16e335a18f8, skipping upload
File with same data already exists at sam-app-with-layers/ef709674620cbb44b9cd95bf6147ed37, skipping upload

        Deploying with following values
        ===============================
        Stack name                   : sam-app-with-layers
        Region                       : us-east-2
        Confirm changeset            : True
        Disable rollback             : False
        Deployment s3 bucket         : aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j
        Capabilities                 : ["CAPABILITY_IAM"]
        Parameter overrides          : {}
        Signing Profiles             : {}

Initiating deployment
=====================
File with same data already exists at sam-app-with-layers/e648f1995a488f66634cf0c9eb8c6016.template, skipping upload

Waiting for changeset to be created..

CloudFormation stack changeset
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Operation                                          LogicalResourceId                                  ResourceType                                       Replacement                                      
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
+ Add                                              HelloWorldFunctionHelloWorldPermissionProd         AWS::Lambda::Permission                            N/A                                              
+ Add                                              HelloWorldFunctionRole                             AWS::IAM::Role                                     N/A                                              
+ Add                                              HelloWorldFunction                                 AWS::Lambda::Function                              N/A                                              
+ Add                                              SampleLayerf40b8e9448                              AWS::Lambda::LayerVersion                          N/A                                              
+ Add                                              ServerlessRestApiDeployment47fc2d5f9d              AWS::ApiGateway::Deployment                        N/A                                              
+ Add                                              ServerlessRestApiProdStage                         AWS::ApiGateway::Stage                             N/A                                              
+ Add                                              ServerlessRestApi                                  AWS::ApiGateway::RestApi                           N/A                                              
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Changeset created successfully. arn:aws:cloudformation:us-east-2:431036401867:changeSet/samcli-deploy1647364103/b48c942f-0634-4677-8111-64bf9e681d6f


Previewing CloudFormation changeset before deployment
======================================================
Deploy this changeset? [y/N]: y

2022-03-15 10:08:43 - Waiting for stack create/update to complete

CloudFormation events from stack operations
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
ResourceStatus                                     ResourceType                                       LogicalResourceId                                  ResourceStatusReason                             
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE_IN_PROGRESS                                 AWS::IAM::Role                                     HelloWorldFunctionRole                             -                                                
CREATE_IN_PROGRESS                                 AWS::IAM::Role                                     HelloWorldFunctionRole                             Resource creation Initiated                      
CREATE_IN_PROGRESS                                 AWS::Lambda::LayerVersion                          SampleLayerf40b8e9448                              -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::LayerVersion                          SampleLayerf40b8e9448                              Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::Lambda::LayerVersion                          SampleLayerf40b8e9448                              -                                                
CREATE_COMPLETE                                    AWS::IAM::Role                                     HelloWorldFunctionRole                             -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Function                              HelloWorldFunction                                 -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Function                              HelloWorldFunction                                 Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::Lambda::Function                              HelloWorldFunction                                 -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::RestApi                           ServerlessRestApi                                  -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::RestApi                           ServerlessRestApi                                  Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::ApiGateway::RestApi                           ServerlessRestApi                                  -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         Resource creation Initiated                      
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         -                                                
CREATE_COMPLETE                                    AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              Resource creation Initiated                      
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         -                                                
CREATE_COMPLETE                                    AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         -                                                
CREATE_COMPLETE                                    AWS::CloudFormation::Stack                         sam-app-with-layers                                -                                                
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CloudFormation outputs from deployed stack
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Outputs                                                                                                                                                                                                  
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Key                 HelloWorldFunctionIamRole                                                                                                                                                            
Description         Implicit IAM Role created for Hello World function                                                                                                                                   
Value               arn:aws:iam::431036401867:role/sam-app-with-layers-HelloWorldFunctionRole-GMTNXC5VMGW3                                                                                               

Key                 HelloWorldApi                                                                                                                                                                        
Description         API Gateway endpoint URL for Prod stage for Hello World function                                                                                                                     
Value               https://47jwyqfc39.execute-api.us-east-2.amazonaws.com/Prod/hello/                                                                                                                   

Key                 HelloWorldFunction                                                                                                                                                                   
Description         Hello World Lambda Function ARN                                                                                                                                                      
Value               arn:aws:lambda:us-east-2:431036401867:function:sam-app-with-layers-HelloWorldFunction-2jxghjyJwvjK                                                                                   
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Successfully created/updated stack - sam-app-with-layers in us-east-2

```
