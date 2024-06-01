package org.homework.services;

import jakarta.xml.bind.JAXBException;
import org.homework.api.CommandService;
import org.homework.api.DataService;
import org.homework.di.annotations.Register;
import org.homework.di.annotations.Resolve;
import org.homework.logger.Logger;
import org.homework.model.ValCurs;
import org.homework.model.Valute;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Register
public class CommandServiceImpl implements CommandService {
    @Resolve
    private DataService dataService;

    @Resolve
    private Logger logger;

    @Override
    public SendMessage getHeroes(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        try {
            logger.info("Запрос данных персонажей для чата: " + chatId);
            sendMessage.setText(dataService.getCharacterData().toString());
        } catch (Exception error) {
            logger.error("Ошибка при получении данных персонажей: " + error.getMessage());

            sendMessage.setText("Произошла ошибка при получении данных. Попробуйте позже.");
        }
        return sendMessage;
    }

    @Override
    public SendMessage getHelp(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Commands: \n /help - помощник,  \n " +
                "/today - получить курс валюты ЦБ РФ на сегодня \n" +
                "/usd - получить курс доллара на сегодня \n" +
                "/euro - получить курс евро на сегодня");
        return sendMessage;
    }

    @Override
    public SendMessage getExchangeRatesToday(String chatId) throws IOException, URISyntaxException {
        List<Valute> valutes = dataService.getValutesToday();

        String s = "";
        for (int i=0; i< valutes.size(); i++) {
            s = s + valutes.get(i).getNominal()
                    + " " + valutes.get(i).getName()
                    + ": " + valutes.get(i).getValue() + " руб.\n";
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Курс валют ЦБ РФ на сегодня: \n \n" + s);

        return sendMessage;
    }

    @Override
    public SendMessage getUSD(String chatId) throws JAXBException, IOException, URISyntaxException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Float usdCourse = dataService.getUSDToday();
        sendMessage.setText("Курс доллара составляет: " + usdCourse.toString() + " руб.");
        return sendMessage;
    }

    @Override
    public SendMessage getEUR(String chatId) throws JAXBException, IOException, URISyntaxException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Float usdCourse = dataService.getEURToday();
        sendMessage.setText("Курс евро составляет: " + usdCourse.toString() + " руб.");
        return sendMessage;
    }
}
