package guru.springframework.spring5webfluxrest.controllers;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
// import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorController vendorController;
    VendorRepository vendorRepository;

    @BeforeEach
    void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void testGetById() {
        BDDMockito.given(vendorRepository.findById("someid")).willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

        webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBody(Vendor.class);
    }

    @Test
    void testList() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(), 
                Vendor.builder().firstName("Barney").lastName("Rubble").build()));

        webTestClient.get().uri("/api/v1/vendors").exchange().expectBodyList(Vendor.class).hasSize(2);
    }

    // @Test
    // void testCreate() {
    //     BDDMockito.given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

    //     Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

    //     webTestClient.post().uri("/api/v1/vendors").body(vendorToSaveMono, Vendor.class).exchange().expectStatus().isCreated();
    // }

    @Test
    void testUpdate(){
        BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().build());

        webTestClient.put().uri("/api/v1/vendors/someid").body(vendorMonoToUpdate, Vendor.class).exchange().expectStatus().isOk();
    }
}
