# "sam deploy --guided --config-file mysamconfig.toml" logs

```shell
phillipr@es-admins-MBP-2 sam-app-with-layers % sam deploy --guided --config-file mysamconfig.toml

Configuring SAM deploy
======================

        Looking for config file [mysamconfig.toml] :  Found
        Reading default arguments  :  Success

        Setting default arguments for 'sam deploy'
        =========================================
        Stack Name [sam-app-with-layers]: 
        AWS Region [us-east-2]: 
        #Shows you resources changes to be deployed and require a 'Y' to initiate deploy
        Confirm changes before deploy [Y/n]: 
        #SAM needs permission to be able to create roles to connect to the resources in your template
        Allow SAM CLI IAM role creation [Y/n]: 
        #Preserves the state of previously provisioned resources when an operation fails
        Disable rollback [y/N]: 
        HelloWorldFunction may not have authorization defined, Is this okay? [y/N]: y
        Save arguments to configuration file [Y/n]: 
        SAM configuration file [mysamconfig.toml]: 
        SAM configuration environment [default]: 

        Looking for resources needed for deployment:
         Managed S3 bucket: aws-sam-cli-managed-default-samclisourcebucket-dm05a8pfwg2j
         A different default S3 bucket can be set in samconfig.toml

        Saved arguments to config file
        Running 'sam deploy' for future deployments will use the parameters saved above.
        The above parameters can be changed by modifying samconfig.toml
        Learn more about samconfig.toml syntax at 
        https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-config.html

Uploading to sam-app-with-layers/9ff5b3f07d1cdbeab8be3634b0ad598f  6605 / 6605  (100.00%)
Uploading to sam-app-with-layers/7c0ad793a397a526b45a92a615808a16  2341 / 2341  (100.00%)

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
Uploading to sam-app-with-layers/845bb587e406bef72be2e0ac3a77d7c5.template  1758 / 1758  (100.00%)

Waiting for changeset to be created..

CloudFormation stack changeset
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Operation                                          LogicalResourceId                                  ResourceType                                       Replacement                                      
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
+ Add                                              HelloWorldFunctionHelloWorldPermissionProd         AWS::Lambda::Permission                            N/A                                              
+ Add                                              HelloWorldFunctionRole                             AWS::IAM::Role                                     N/A                                              
+ Add                                              HelloWorldFunction                                 AWS::Lambda::Function                              N/A                                              
+ Add                                              SampleLayer69f50a5ded                              AWS::Lambda::LayerVersion                          N/A                                              
+ Add                                              ServerlessRestApiDeployment47fc2d5f9d              AWS::ApiGateway::Deployment                        N/A                                              
+ Add                                              ServerlessRestApiProdStage                         AWS::ApiGateway::Stage                             N/A                                              
+ Add                                              ServerlessRestApi                                  AWS::ApiGateway::RestApi                           N/A                                              
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Changeset created successfully. arn:aws:cloudformation:us-east-2:431036401867:changeSet/samcli-deploy1647365529/88a918fb-8892-4018-92b0-6a6c90222d6a


Previewing CloudFormation changeset before deployment
======================================================
Deploy this changeset? [y/N]: y

2022-03-15 10:32:20 - Waiting for stack create/update to complete

CloudFormation events from stack operations
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
ResourceStatus                                     ResourceType                                       LogicalResourceId                                  ResourceStatusReason                             
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE_IN_PROGRESS                                 AWS::IAM::Role                                     HelloWorldFunctionRole                             -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::LayerVersion                          SampleLayer69f50a5ded                              -                                                
CREATE_IN_PROGRESS                                 AWS::IAM::Role                                     HelloWorldFunctionRole                             Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::Lambda::LayerVersion                          SampleLayer69f50a5ded                              -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::LayerVersion                          SampleLayer69f50a5ded                              Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::IAM::Role                                     HelloWorldFunctionRole                             -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Function                              HelloWorldFunction                                 -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Function                              HelloWorldFunction                                 Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::Lambda::Function                              HelloWorldFunction                                 -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::RestApi                           ServerlessRestApi                                  -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::RestApi                           ServerlessRestApi                                  Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::ApiGateway::RestApi                           ServerlessRestApi                                  -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              -                                                
CREATE_IN_PROGRESS                                 AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         Resource creation Initiated                      
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::ApiGateway::Deployment                        ServerlessRestApiDeployment47fc2d5f9d              -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         -                                                
CREATE_COMPLETE                                    AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         -                                                
CREATE_IN_PROGRESS                                 AWS::ApiGateway::Stage                             ServerlessRestApiProdStage                         Resource creation Initiated                      
CREATE_COMPLETE                                    AWS::Lambda::Permission                            HelloWorldFunctionHelloWorldPermissionProd         -                                                
CREATE_COMPLETE                                    AWS::CloudFormation::Stack                         sam-app-with-layers                                -                                                
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

CloudFormation outputs from deployed stack
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Outputs                                                                                                                                                                                                  
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Key                 HelloWorldFunctionIamRole                                                                                                                                                            
Description         Implicit IAM Role created for Hello World function                                                                                                                                   
Value               arn:aws:iam::431036401867:role/sam-app-with-layers-HelloWorldFunctionRole-RSLQZT30679A                                                                                               

Key                 HelloWorldApi                                                                                                                                                                        
Description         API Gateway endpoint URL for Prod stage for Hello World function                                                                                                                     
Value               https://u1l3tmjcd5.execute-api.us-east-2.amazonaws.com/Prod/hello/                                                                                                                   

Key                 HelloWorldFunction                                                                                                                                                                   
Description         Hello World Lambda Function ARN                                                                                                                                                      
Value               arn:aws:lambda:us-east-2:431036401867:function:sam-app-with-layers-HelloWorldFunction-HkyST5I9mIWj                                                                                   
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Successfully created/updated stack - sam-app-with-layers in us-east-2

```