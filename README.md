# sam-app-with-layers

This projects shows up SAM issue related to building `AWS::Serverless::LayerVersion` after `AWS::Serverless::Function`.

## How to reproduce it:

1. `sam build` will show the wrong order:

```shell
$ sam build
Building function 'HelloWorldFunction'
Running JavaMavenWorkflow:CopySource
Running JavaMavenWorkflow:MavenBuild

Build Failed
Error: JavaMavenWorkflow:MavenBuild - Maven Failed: [INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< helloworld:HelloWorld >------------------------
[INFO] Building A sample Hello World created for SAM CLI. 1.0
[INFO] --------------------------------[ jar ]---------------------------------
[WARNING] The POM for helloworld:sample-layer:jar:1.0-SNAPSHOT is missing, no dependency information available
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.130 s
[INFO] Finished at: 2020-06-10T18:38:09-03:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal on project HelloWorld: Could not resolve dependencies for project helloworld:HelloWorld:jar:1.0: Could not find artifact helloworld:sample-layer:jar:1.0-SNAPSHOT -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/DependencyResolutionException
``` 

## How to fix it?

1. `sam build SampleLayer` to build layer first and make it available on local maven repository

1. `sam build` to build others project resources

