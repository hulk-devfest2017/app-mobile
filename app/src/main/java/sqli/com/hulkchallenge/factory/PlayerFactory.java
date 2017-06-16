package sqli.com.hulkchallenge.factory;

import java.util.Collections;
import java.util.List;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Gender;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;
import freemarker.template.utility.CollectionUtils;
import sqli.com.hulkchallenge.model.Player;

/**
 * Created by renaud on 16/06/17.
 */

public class PlayerFactory {


    public static  Player getPlayer(VCard vCard){
        Player player = new Player();
        if(vCard != null){
            List<Email> emails = vCard.getEmails();
            if (emails != null && emails.size() > 0){
                player.setEmail(emails.get(0).getValue());
            }
            StructuredName structuredName = vCard.getStructuredName();
            if(structuredName != null){
                player.setLastName(structuredName.getFamily());
                player.setFirstName(structuredName.getGiven());
            }
            Organization organizations = vCard.getOrganization();
            if(organizations != null && organizations.getValues() != null && organizations.getValues().size() > 0){
                player.setCompany(organizations.getValues().get(0));
            }

            Gender gender = vCard.getGender();
            if(gender != null){
                player.setMale(gender.isMale());
            }
        }
        return player;
    }
}
