package com.heuristica.ksroutewinthor.processor;

import com.github.kevinsawicki.http.HttpRequest;
import com.heuristica.ksroutewinthor.api.Order;
import com.heuristica.ksroutewinthor.model.Pedido;
import com.heuristica.ksroutewinthor.model.PedidoRepository;
import static com.heuristica.ksroutewinthor.util.JavaUtil.defaultJsonb;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderProcessor extends ApplicationProcessor {

    @Autowired PedidoRepository pedidoRepository;

    public Pedido processPedido(Pedido pedido) throws IOException {
        Order order = dozerBeanMapper.map(pedido, Order.class);
        String orderJson = defaultJsonb().toJson(order);
        if (pedido.getKsrId() == null) {
            sendCreateRequest(pedido, orderJson);
        } else {
            sendUpdateRequest(pedido.getKsrId(), orderJson);
        }
        return pedido;
    }

    private void sendCreateRequest(Pedido pedido, String orderJson) throws IOException {
        HttpRequest request = HttpRequest.post(apiUrl + "/orders.json");
        request = request.contentType("application/json", "utf-8");
        request = request.header("X-User-Email", apiEmail).header("X-User-Token", apiToken);
        request.send("{\"order\":" + orderJson + "}");
        if (request.ok()) {
            persistResponse(pedido, request.body());
        } else {
            throw new IOException(String.format("Erro no pedido %d, detail: %s", pedido.getNumped(), request.body()));
        }
    }

    private void sendUpdateRequest(Long id, String orderJson) throws IOException {
        HttpRequest request = HttpRequest.put(apiUrl + "/orders/" + id + ".json");
        request = request.contentType("application/json", "utf-8");
        request = request.header("X-User-Email", apiEmail).header("X-User-Token", apiToken);
        request.send("{\"order\": {" + orderJson + "}}");
        if (!request.ok()) {
            throw new IOException(String.format("Erro no pedido %d, detail: %s", id, request.body()));
        }
    }

    private void persistResponse(Pedido pedido, String response) {
        Order resOrder = defaultJsonb().fromJson(response, Order.class);
        pedido.setKsrId(resOrder.getId());
        pedidoRepository.save(pedido);
    }

}
