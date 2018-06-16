from mongo:3.2

# Copy Entrypoint script in the container
COPY ./docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]
RUN ["chmod", "+x", "/docker-entrypoint.sh"]
EXPOSE 27017
CMD ["mongod"]
