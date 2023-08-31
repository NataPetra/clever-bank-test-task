package by.nata.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Config {

    private Integer interestToBeCharged;
    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{");
        sb.append("interestToBeCharged=").append(interestToBeCharged);
        sb.append(", dbDriver='").append(dbDriver).append('\'');
        sb.append(", dbUrl='").append(dbUrl).append('\'');
        sb.append(", dbUser='").append(dbUser).append('\'');
        sb.append(", dbPassword='").append(dbPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
