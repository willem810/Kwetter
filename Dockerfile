FROM airhacks/glassfish
COPY ./target/kwetter.war ${DEPLOYMENT_DIR}
