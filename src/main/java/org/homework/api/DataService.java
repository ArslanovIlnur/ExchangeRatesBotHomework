package org.homework.api;

import jakarta.xml.bind.JAXBException;
import org.homework.model.CharacterData;
import org.homework.model.Valute;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface DataService {
    List<CharacterData> getCharacterData() throws IOException, URISyntaxException;
    List<Valute> getValutesToday() throws IOException, URISyntaxException;
    Float getUSDToday() throws IOException, URISyntaxException, JAXBException;
    Float getEURToday() throws IOException, URISyntaxException, JAXBException;
}
