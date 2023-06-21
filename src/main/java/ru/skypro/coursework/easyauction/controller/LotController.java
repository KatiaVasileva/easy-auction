package ru.skypro.coursework.easyauction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.coursework.easyauction.model.dto.BidDTO;
import ru.skypro.coursework.easyauction.model.dto.CreateBid;
import ru.skypro.coursework.easyauction.model.dto.CreateLot;
import ru.skypro.coursework.easyauction.model.dto.LotDTO;
import ru.skypro.coursework.easyauction.model.projections.FullLot;
import ru.skypro.coursework.easyauction.service.LotService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lot")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    // 1. Создание нового лота
    @PostMapping
    public void createLot(@Valid @RequestBody CreateLot createLot) {
        lotService.createLot(createLot);
    }

    // 2. Начать торги по лоту
    @PostMapping("/{id}/start")
    public void startBidding(@PathVariable int id) {
        lotService.startBidding(id);
    }

    // 3. Сделать ставку по лоту
    @PostMapping("/{id}/bid")
    public void bid(@PathVariable int id, @Valid @RequestBody CreateBid bid) {
        lotService.bid(id, bid);
    }

    // 4. Остановить торги по лоту
    @PostMapping("/{id}/stop")
    public void stopBidding(@PathVariable int id) {
        lotService.stopBidding(id);
    }

    // 5. Получить информацию о первом ставившем на лот
    @GetMapping("/{id}/first")
    public BidDTO getFirstBidder(@PathVariable int id) {
        return lotService.getFirstBidder(id);
    }

    // 6. Получить имя ставившего на данный лот наибольшее количество раз
    @GetMapping("/{id}/frequent")
    public BidDTO getMostFrequentBidder(@PathVariable int id) {
        return lotService.getMostFrequentBidder(id);
    }

    // 7. Получить полную информацию о лоте
    @GetMapping("/{id}")
    public FullLot getFullLot(@PathVariable int id) {
        return lotService.getFullLot(id);
    }

    // 8. Получить все лоты, основываясь на фильтре статуса и номере страницы
    @GetMapping("")
    public List<LotDTO> findLots(@RequestParam(value = "page", required = false) Integer pageIndex,
                                  @RequestParam(value = "status") String status) {
        return lotService.findLots(pageIndex, status);
    }

    // 9. Экспортировать все лоты в файл CSV
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getCSVFile() throws IOException {
        return lotService.getCSVFile();
    }
}
