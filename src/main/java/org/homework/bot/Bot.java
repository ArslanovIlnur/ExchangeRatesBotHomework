package org.homework.bot;

import jakarta.xml.bind.JAXBException;
import org.homework.api.CommandService;
import org.homework.logger.Logger;
import org.homework.di.annotations.Register;
import org.homework.di.annotations.Resolve;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;

@Register
public class Bot extends TelegramLongPollingBot {
    @Resolve
    private CommandService commandService;
    @Resolve
    private Logger logger;
    @Override
    public String getBotUsername() {
        // Верните имя вашего бота
        return "ExchangeRatesHomeworkBot";
    }

    @Override
    public String getBotToken() {
        return "6551663864:AAHY8-l18_LXAM0iOIAdoJ_oua5dpLiDsmM";
    }


    @Override
    public void onUpdateReceived(Update update) {
        logger.debug("Получено новое обновление: " + update.toString());

        // Проверяем, есть ли в обновлении сообщение и текст сообщения
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            logger.info("Обработка команды: " + command);

            try {
                if (command.equalsIgnoreCase("/getHeroes")) {
                    // Вызов метода getHeroes из CommandService
                    execute(commandService.getHeroes(chatId));

                } else if (command.equalsIgnoreCase("/help")) {
                    // Вызов метода getHelp из CommandService
                     execute(commandService.getHelp(chatId));
                } else if (command.equalsIgnoreCase("/today")) {
                    execute(commandService.getExchangeRatesToday(chatId));
                } else if (command.equalsIgnoreCase("/usd")) {
                    execute(commandService.getUSD(chatId));
                } else if (command.equalsIgnoreCase("/euro")) {
                    execute(commandService.getEUR(chatId));
                } else if (command.equalsIgnoreCase("/start")) {
                    execute(commandService.getHelp(chatId));
                }
                // Отправляем сообщение
            } catch (TelegramApiException | IOException | URISyntaxException | JAXBException e) {
                logger.error("Ошибка при отправке сообщения в Telegram: " + e.getMessage());
            }
        }
    }
}