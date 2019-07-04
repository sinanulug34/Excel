package com.example.demo.domain;


public class LoginType {

    enum Type {
        PremiumWithDownload, PremiumWithoutDownload, NonPremiumWithDownload, NonPremiumWithoutDownload,
        RadioWithDownload, RadioWithoutDownload, Guest
    }

    static public LoginType.Type getTypeFromExcel(String pack, String download) {
        if (pack.contains("Misafir")) {
            return Type.Guest;
        } else if (pack.contains("Premium")) {
            if (download.contains("withDownload"))
                return Type.PremiumWithDownload;
            else
                return Type.PremiumWithoutDownload;
        } else if (pack.contains("Başlangıç")) {
            if (download.contains("withDownload"))
                return Type.NonPremiumWithDownload;
            else
                return Type.NonPremiumWithoutDownload;
        } else if (pack.contains("Radyo")) {
            if (download.contains("withDownload"))
                return Type.RadioWithDownload;
            else
                return Type.RadioWithoutDownload;
        } else {
            return null;
        }
    }


    static public LoginType.Type getTypeFromJson(String steps) {
        if (steps.contains("I log in as guest")) {
            return Type.Guest;
        } else if (steps.contains("I log in as any premium user with download package")) {
            return Type.PremiumWithDownload;
        } else if (steps.contains("I log in as any premium user without download package")) {
            return Type.PremiumWithoutDownload;
        } else if (steps.contains("I log in as any nonpremium user with download package")) {
            return Type.NonPremiumWithDownload;
        } else if (steps.contains("I log in as any nonpremium user without download package")) {
            return Type.NonPremiumWithoutDownload;
        } else if (steps.contains("I log in as any radio user with download package")) {
            return Type.RadioWithDownload;
        } else if (steps.contains("I log in as any radio user without download package")) {
            return Type.RadioWithoutDownload;
        } else {
            return null;
        }
    }


}
