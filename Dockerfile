FROM airhacks/glassfish
COPY ./target/javaee8-essentials-archetype.war ${DEPLOYMENT_DIR}
