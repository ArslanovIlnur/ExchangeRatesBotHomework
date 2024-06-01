package org.homework.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.homework.logger.Logger;
import org.homework.model.CharacterData;
import org.homework.model.CharacterResponse;
import org.homework.api.DataService;
import org.homework.api.HttpService;
import org.homework.di.annotations.Register;
import org.homework.di.annotations.Resolve;
import org.homework.model.ValCurs;
import org.homework.model.Valute;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Класс, представляющий полную структуру ответа API
// Использование сервиса для получения данных
@Register
public class DataServiceImpl implements DataService {
    @Resolve
    private Logger logger;
    @Resolve
    private HttpService httpService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<CharacterData> getCharacterData() throws IOException, URISyntaxException {
        logger.debug("Запрос данных персонажей с API");

        String url = "https://rickandmortyapi.com/api/character?page=1";
        String jsonResponse = httpService.sendGetRequest(url, Map.of());

        CharacterResponse response = objectMapper.readValue(jsonResponse, CharacterResponse.class);
        return response.results;
    }

    @Override
    public List<Valute> getValutesToday() throws IOException, URISyntaxException {
        logger.debug("Получение курса валют на сегодня");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);

        List<Valute> valutes = null;

        String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + today;
        String xml = httpService.sendGetRequest(url, Map.of());

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader stringReader = new StringReader(xml);
            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(stringReader);

            valutes = valCurs.getValutes();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return valutes;
    }

    @Override
    public Float getUSDToday() throws IOException, URISyntaxException, JAXBException {
        logger.debug("Получение курса доллара на сегодня");
        Float usdCourse = null;

        Valute valute = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);

        List<Valute> valutes = null;

        String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + today;
        String xml = httpService.sendGetRequest(url, Map.of());

        JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader stringReader = new StringReader(xml);
        ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(stringReader);

        valutes = valCurs.getValutes();

        for (int i=0; i< valutes.size(); i++){
            if (valutes.get(i).getCharCode().equals("USD")){
                valute = valutes.get(i);
            }
        }


        usdCourse = Float.valueOf(valute.getValue().replace(',','.'));
        return usdCourse;
    }

    @Override
    public Float getEURToday() throws IOException, URISyntaxException, JAXBException {
        logger.debug("Получение курса евро на сегодня");

        Float euroCourse = null;
        Valute valute = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);

        List<Valute> valutes = null;

        String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + today;
        String xml = httpService.sendGetRequest(url, Map.of());

        JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader stringReader = new StringReader(xml);
        ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(stringReader);

        valutes = valCurs.getValutes();

        for (int i=0; i< valutes.size(); i++){
            if (valutes.get(i).getCharCode().equals("EUR")){
                valute = valutes.get(i);
            }
        }


        euroCourse = Float.valueOf(valute.getValue().replace(',','.'));
        return euroCourse;
    }
}
