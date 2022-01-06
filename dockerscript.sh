#/bin/bash
gradle build
docker build -t matching-engine:1 .
docker tag matching-engine:1 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:1
docker push 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:1
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Update ECR/EC2 and run container" --parameters commands="docker stop $(docker ps -q)" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Update ECR/EC2 and run container" --parameters commands="docker pull 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:1" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Update ECR/EC2 and run container" --parameters commands="docker run -p 8080:8080 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:1" --region us-east-2 --output text
