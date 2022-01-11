./gradlew shadowJar
rsync -avzh --delete build/libs/arkiv-backend-1.0-SNAPSHOT-all.jar bogaeus:/home/henry/arkiv-backend/jars/

ssh -t henry@bogaeus "sudo systemctl restart arkiv-backend.service"
