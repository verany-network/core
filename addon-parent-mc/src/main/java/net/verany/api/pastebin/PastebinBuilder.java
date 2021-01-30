package net.verany.api.pastebin;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PastebinBuilder {

    private final PastebinFactory factory = new PastebinFactory();
    private final Pastebin pastebin = factory.createPastebin("puXwIEKj5dehPWLyh9NnmM7GmFRE6fWW");

    private final String title;
    private final String raw;
    private final PasteVisiblity visibility;
    private final PasteExpire expire;

    public Response<String> post() {
        PasteBuilder builder = factory.createPaste();
        builder.setTitle(title);
        builder.setRaw(raw);
        builder.setVisiblity(visibility);
        builder.setExpire(expire);

        Paste paste = builder.build();
        Response<String> postResult = pastebin.post(paste);
        if (postResult.hasError()) {
            System.out.println("PASTE FAILED");
            return postResult;
        }
        System.out.println("Paste pushed");
        return postResult;
    }

}
