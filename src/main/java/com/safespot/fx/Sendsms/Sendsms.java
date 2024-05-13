package com.safespot.fx.Sendsms;

import com.twilio.Twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Sendsms {

    public static final String ACCOUNT_SID = System.getenv("ACec64e15275b803dc64e8ecb0b4773a5d");
    public static final String AUTH_TOKEN = System.getenv("3cbe4f7b7ec433939385c915e93fddb0");


    public static void sendSMS() {
        Twilio.init("ACec64e15275b803dc64e8ecb0b4773a5d", "3cbe4f7b7ec433939385c915e93fddb0");
        Message message = Message.creator(new PhoneNumber("+21620160141"),
                new PhoneNumber("+12176357154"),
                "Reponse ajoutée").create();


        System.out.println(message.getSid());
    }
}
