# Selenium Framework with Saucelabs,Docker and Zalenium
This framework can be extented to any application. 
1) It comes with default docker support to run test cases in parallel using grid infrastructure within the Docker
2) It has saucelabs support as well. Ensure to set your username and access key at global level for sauce level
3) Zalenium scirpts as well are integrated
To run Zalenium, ensure to open cmd as admin user and run below command
docker run --rm -ti --name zalenium -p 4444:4444 -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos --privileged dosel/zalenium start 


