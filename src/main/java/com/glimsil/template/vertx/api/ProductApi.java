package com.glimsil.template.vertx.api;

import com.glimsil.template.vertx.api.dto.ProductDto;
import com.glimsil.template.vertx.service.ProductService;
import com.glimsil.template.vertx.config.annotation.Api;
import com.glimsil.template.vertx.config.annotation.Endpoint;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


@Api("/product")
public class ProductApi {

    ProductService productService = ProductService.getInstance();

    @Endpoint(method = HttpMethod.GET)
    public void getProduct(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        JsonObject messageJson = JsonObject.mapFrom(productService.getProduct());
        if (messageJson == null) {
            sendError(404, response);
        } else {
            response.putHeader("content-type", "application/json").end(messageJson.encodePrettily());
        }

    }

    @Endpoint(method = HttpMethod.POST)
    public void saveProduct(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        ProductDto bodyMessage = body.mapTo(ProductDto.class);
        HttpServerResponse response = routingContext.response();
        if (bodyMessage == null) {
            sendError(400, response);
        } else {
            JsonObject messageJson = JsonObject.mapFrom(productService.saveProduct(bodyMessage.getId(), bodyMessage.getName()));
            if (messageJson == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(messageJson.encodePrettily());
            }
        }
    }

    @Endpoint(method = HttpMethod.GET, path = "/hello/world")
    public void hello(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.end(productService.getHelloWorld());
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
