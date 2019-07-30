package views;

import uk.co.caprica.vlcj.player.base.Logo;
import uk.co.caprica.vlcj.player.base.LogoPosition;

public class MyLogo {
    private Logo logo =Logo.logo().file("picture/logo.png").position(LogoPosition.TOP_LEFT).opacity(0.2f).enable();
    public  Logo getLogo(){
        return logo;
    }
}
