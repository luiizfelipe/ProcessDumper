package com.processdumper.model.mapinfo;

public class InvalidMapFormatException extends RuntimeException { public InvalidMapFormatException(String mapData) { super("Não foi possível criar MapInfo: formato inválido -> " + mapData); } }