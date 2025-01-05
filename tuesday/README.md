# welcome to the Tuesday Assignment

Current tasks will help you to practice docker usage.

- [Task 1](#task-1)
- [Task 2](#task-2)
- [Task 3](#task-3)
- [Task 4](#task-4)

## Task 1

Run a simple [httpd](https://hub.docker.com/_/httpd) container with an exposed `8080` port. To check if it runs
correctly just open `localhost:8080`

- review the logs of the running container
- use `docker exec <container_id> bash` to inspect the container. Check the package where sources are placed:
  `/usr/local/apache2/htdocs/`. (useful commands: `pwd`, `ls`, `cat`, to exit use `exit` command)
- update the found `index.html` content with any desired changes.
- verify that the httpd server shows different content according to your changes.

_Hint. To update the content you can either use:_

- `docker cp <source> CONTAINER:<destination>` command to copy the file from the host to the container.
- `cat >> [file] << [your text]` to update the existing file.
  _Example:_

```bash
cat >> /usr/local/apache2/htdocs/index.html << EOF
<h1>hello world</h1>
EOF
```

## Task 2

Pull and run a [custom httpd](https://hub.docker.com/repository/docker/nourish1709/codeus-practice/general) container
with exposed `8080` port. To check if it runs correctly just open `localhost:8080`

- discover why does the current container return a different response. use `docker exec bash` to inspect the container.
  check the `/usr/local/apache2/htdocs/` and see the resources

## Task 3

Run a [httpd](https://hub.docker.com/_/httpd) container with resources provided in the [static](./static) folder.
Ensure that the resources are available on the `localhost:8080` endpoint and the response is the same as in the "Task
2". Use the following different strategies to achieve this:

- using [Dockerfile](https://docs.docker.com/reference/dockerfile/)
- using `docker run` along with [volumes](https://docs.docker.com/engine/storage/volumes/#options-for---volume)

## Task 4

Create your own customized container and deploy it to a running Tomcat container

- create a `Dockerfile` that is based on `tomcat:10.1` and copies the web application archive (`.war`) file to the
  `/usr/local/tomcat/webapps/` directory

Note that the destination `.war` filename is used as the base path for the servlet. For example, if you copy create a
`/usr/local/tomcat/webapps/my_servlet.war` file, the servlet will be available at `localhost:8080/my_servlet/`
