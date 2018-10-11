package net.ingtra.pyokt;


import org.openkoreantext.processor.KoreanPosJava;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.openkoreantext.processor.tokenizer.Sentence;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class OpenKoreanTextWrapper {
    public static String normalize(String text) {
        return OpenKoreanTextProcessorJava.normalize(text).toString();
    }

    public static KoreanTokenJava[] tokenize(String text, Boolean normalize, Boolean stem) {
        if (normalize) {
            text = OpenKoreanTextWrapper.normalize(text);
        }

        Seq<KoreanTokenizer.KoreanToken> scalaTokens = OpenKoreanTextProcessorJava.tokenize(text);
        List<KoreanTokenJava> tokens = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(scalaTokens);
        LinkedList<KoreanTokenJava> javaTokens = new LinkedList<>();

        for (KoreanTokenJava token : tokens) {
            String tokenText = ((stem && !token.getStem().isEmpty()) ? token.getStem() : token.getText());
            KoreanTokenJava javaToken = new KoreanTokenJava(
                    tokenText,
                    KoreanPosJava.valueOf(token.getPos().toString()),
                    token.getOffset(),
                    token.getLength(),
                    token.isUnknown(),
                    token.getStem()
            );

            javaTokens.add(javaToken);
        }

        return javaTokens.toArray(new KoreanTokenJava[0]);
    }

    public static String[] splitSentences(String text) {
        List<Sentence> sentences = OpenKoreanTextProcessorJava.splitSentences(text);
        LinkedList<String> sentenceStrings = new LinkedList<>();

        for (Sentence s : sentences) {
            sentenceStrings.add(s.text());
        }

        return sentenceStrings.toArray(new String[0]);
    }

    public static void addNounsToDictionary(String[] list) {
        OpenKoreanTextProcessorJava.addNounsToDictionary(Arrays.asList(list));
    }

    public static KoreanPhraseExtractor.KoreanPhrase[] extractPhrases(String text, Boolean filterSpam, Boolean addHashtags) {
        text = OpenKoreanTextWrapper.normalize(text);
        Seq<KoreanTokenizer.KoreanToken> scalaTokens = OpenKoreanTextProcessorJava.tokenize(text);
        List<KoreanPhraseExtractor.KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(scalaTokens, filterSpam, addHashtags);

        return phrases.toArray(new KoreanPhraseExtractor.KoreanPhrase[0]);
    }
}
