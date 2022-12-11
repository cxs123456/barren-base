FROM openjdk:8u275
# 作者
MAINTAINER barren
# 创建日志输出目录logs并且
RUN mkdir -p /app/logs
# 创建/app/logs挂载点到宿主机目录/var/lib/docker下，该命令无法指定挂载点对应宿主机目录，推荐使用docker run -v
VOLUME ["/app/logs"]
# cd 到容器/app目录中
WORKDIR app
# 将当前目录下target目录*.jar全部复制到 容器当前目录app中并且重命名app.jar
ADD ./target/*.jar ./app.jar
EXPOSE 8081
ENV JVM_OPTS="-Xms1024m -Xmx1024m -Xss1m -Dfile.encoding=UTF-8"
CMD ["java", "--spring.profiles.active=dev", "-jar", "app.jar"]

# springboot分层打包，提升构建docker镜像速度，注意：springboot的版本需要>2.3.x
FROM openjdk:8u275
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]