package org.homework.api;

import jakarta.xml.bind.JAXBException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CommandService {
    SendMessage getHeroes(String chatId);
    SendMessage getHelp(String chatId);
    SendMessage getExchangeRatesToday(String chatId) throws IOException, URISyntaxException;
    SendMessage getUSD(String chatId) throws JAXBException, IOException, URISyntaxException;
    SendMessage getEUR(String chatId) throws JAXBException, IOException, URISyntaxException;
}
