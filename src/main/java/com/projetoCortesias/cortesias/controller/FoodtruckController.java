package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.Coordenada;
import com.projetoCortesias.cortesias.dto.FoodtruckDTO;
import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import com.projetoCortesias.cortesias.service.CoordenadasService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FoodtruckController {

    @Autowired
    private EventoRepository eventoRepo;

    @GetMapping("/mapa-foodtrucks")
    public List<FoodtruckDTO> getFoodtrucksPorData(@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        CoordenadasService coordenadasService = new CoordenadasService();

        EventoRepository eventoRepo = this.eventoRepo;
        List<Evento> eventos = eventoRepo.findByDataEventoBetween(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(data.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)));

        List<FoodtruckDTO> dtos = new ArrayList<>();

        for (Evento evento : eventos) {
            FoodtruckDTO dto = new FoodtruckDTO();
            dto.setNome(evento.getResponsavel());
            dto.setCidade(evento.getLocal());
            Coordenada coordenada = coordenadasService.buscarCoordenadaPorCidade(evento.getLocal());
            dto.setLatitude(coordenada.latitude);
            dto.setLongitude(coordenada.longitude);

            dtos.add(dto);
        }

        return dtos;
    }
}
