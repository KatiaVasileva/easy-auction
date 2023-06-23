package ru.skypro.coursework.easyauction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.coursework.easyauction.model.dto.*;
import ru.skypro.coursework.easyauction.service.LotService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lot")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @PostMapping
    @Operation(summary = "Создание нового лота", description = "Метод создания нового лота, " +
            "если есть ошибки в полях объекта лота - то нужно вернуть статус с ошибкой.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Лот успешно создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CreateLot.class))}),
            @ApiResponse(
                    responseCode = "400", description = "Лот передан с ошибкой")
    })
    public void createLot(@Valid @RequestBody CreateLot createLot) {
        lotService.createLot(createLot);
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Начать торги по лоту", description = "Переводит лот в состояние \"начато\",  " +
            "которое позволяет делать ставки на лот. Если лот уже находится в состоянии \"начато\", " +
            "то ничего не делает и возвращает 200.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Лот переведен в статус начато"),
            @ApiResponse(
                    responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден"
            )
    })
    public void startBidding(@PathVariable int id) {
        lotService.startBidding(id);
    }


    @PostMapping("/{id}/bid")
    @Operation(summary = "Сделать ставку по лоту", description = "Создает новую ставку по лоту. " +
            "Если лот в статусе CREATED или STOPPED, то должна вернутся ошибка.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Ставка создана"),
            @ApiResponse(
                    responseCode = "400", description = "Лот в неверном статусе"),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден"
            )
    })
    public void bid(@PathVariable int id, @Valid @RequestBody CreateBid bid) {
        lotService.bid(id, bid);
    }

    @PostMapping("/{id}/stop")
    @Operation(summary = "Остановить торги по лоту", description = "Переводит лот в состояние \"остановлен\", " +
            "Если лот в статусе CREATED или STOPPED, то должна вернутся ошибка.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Лот перемещен в статус остановлен"),
            @ApiResponse(
                    responseCode = "400", description = "Лот в неверном статусе"),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден"
            )
    })
    public void stopBidding(@PathVariable int id) {
        lotService.stopBidding(id);
    }

    @GetMapping("/{id}/first")
    @Operation(summary = "Получить информацию о первом ставившем на лот",
            description = "Возвращает первого ставившего на этот лот")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Имя первого ставившего и дата первой ставки", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BidDTO.class))}),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден")
    })
    public BidDTO getFirstBidder(@PathVariable int id) {
        return lotService.getFirstBidder(id);
    }

    @GetMapping("/{id}/frequent")
    @Operation(summary = "Возвращает имя ставившего на данный лот наибольшее количество раз",
            description = "Наибольшее количество вычисляется из общего количества ставок на лот")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Имя первого ставившего и дата его последней ставки", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BidDTO.class))}),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден")
    })
    public BidDTO getMostFrequentBidder(@PathVariable int id) {
        return lotService.getMostFrequentBidder(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить полную информацию о лоте",
            description = "Возвращает полную информацию о лоте с последним ставившим и текущей ценой")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Полный лот по идентификатору", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullLotDTO.class))}),
            @ApiResponse(
                    responseCode = "404", description = "Лот не найден")
    })
    public FullLotDTO getFullLot(@PathVariable int id) {
        return lotService.getFullLot(id);
    }

    @GetMapping("")
    @Operation(summary = "Получить все лоты, основываясь на фильтре статуса и номере страницы",
            description = "Возвращает все записи о лотах без информации о текущей цене и победителе " +
                    "постранично.Если страница не указана, то возвращается первая страница. " +
                    "Номера страниц начинаются с 0. Лимит на количество лотов на странице - 10 штук. " +
                    "Если страница не указана, возвращает первую страницу.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Список лотов", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LotDTO.class))}),
    })
    public List<LotDTO> findLots(@RequestParam(value = "page", required = false) Integer pageIndex,
                                  @RequestParam(value = "status") String status) {
        return lotService.findLots(pageIndex, status);
    }

    @GetMapping(value = "/export", produces = "application/csv")
    @Operation(summary = "Экспортировать все лоты в файл CSV", description = "Экспортировать все лоты " +
            "в формате id,title,status,lastBidder,currentPrice в одном файле CSV .")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CSV Report", content = {
                    @Content(mediaType = "application/csv")})})
    public ResponseEntity<Resource> getCSVFile() throws IOException {
        return lotService.getCSVFile();
    }
}
