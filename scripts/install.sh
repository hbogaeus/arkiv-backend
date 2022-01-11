set -x
# Copy systemd file
rsync -avzh scripts/arkiv-backend.service scripts/start.sh bogaeus:/home/henry/arkiv-backend/
# symlink it to /etc/systemd/system and restart
ssh -t henry@bogaeus "sudo ln -s /home/henry/arkiv-backend/arkiv-backend.service /etc/systemd/system/;sudo systemctl enable arkiv-backend.service"
