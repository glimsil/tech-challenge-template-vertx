package com.glimsil.template.vertx;

import com.glimsil.template.vertx.config.RouteConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}

	@Override
	public void start() {
		RouteConfig routeConfig = new RouteConfig();
		Router router = routeConfig.configRoutes(vertx);
		vertx.createHttpServer().requestHandler(router).listen(8080);
	}

}
