# arkiv-backend

## Deployment and operations
The service is managed by Systemd, described in the unit file under the `scripts` folder.

Initial setup is done by the `install.sh` script, which copies over the unit file, the start script and enables the systemd service.

### Deployment

Deploy new versions by running the `deploy.sh` script.

## Logging
The application logs to `/var/log/arkiv-backend`, as configured in `logback.xml`.

