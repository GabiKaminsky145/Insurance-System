package com.insurance.insurance_system.controllers;

import com.insurance.insurance_system.dto.ClientDTOs.BuyRequest;
import com.insurance.insurance_system.dto.ClientDTOs.IdentifyRequest;
import com.insurance.insurance_system.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Client API", description = "Client identification and product assignment")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @Operation(summary = "Identify or create client and return products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/identify")
    public ResponseEntity<?> identify(@RequestBody IdentifyRequest req) {

        var res = service.identifyOrCreate(
                req.clientId(),
                req.name(),
                req.contactMethods(),
                req.authMethodType(),
                req.authMethodValue()
        );

        return ResponseEntity.ok(Map.of(
                "clientId", res.clientId(),
                "products", res.products()
        ));
    }

    @Operation(summary = "Assign product to authenticated client")
    @PostMapping("/{clientId}/products/{productId}/buy")
    public ResponseEntity<?> buy(@PathVariable String clientId,
                                 @PathVariable String productId,
                                 @RequestBody BuyRequest req) {

        service.buyProduct(
                clientId,
                req.authMethodType(),
                req.authMethodValue(),
                productId
        );

        return ResponseEntity.ok("Product assigned");
    }
}