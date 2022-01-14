#/bin/bash
gradle build
docker build -t matching-engine:latest .
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 032044580362.dkr.ecr.us-east-2.amazonaws.com
aws ecr batch-delete-image --repository-name matching-engine --image-ids imageTag=latest --output text
docker tag matching-engine:latest 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine
docker push 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:latest
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Auth ec2 with ecr" --parameters commands="aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 032044580362.dkr.ecr.us-east-2.amazonaws.com" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Stop  docker container" --parameters commands="docker stop \$(docker ps -q)" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Docker delete images" --parameters commands="docker rmi -f 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:latest" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Pull new docker image" --parameters commands="docker pull 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:latest" --region us-east-2 --output text
aws ssm send-command --instance-ids i-07b42278c0722a2d8 --document-name "AWS-RunShellScript" --comment "Run container" --parameters commands="docker run -p 8080:8080 032044580362.dkr.ecr.us-east-2.amazonaws.com/matching-engine:latest" --region us-east-2 --output text