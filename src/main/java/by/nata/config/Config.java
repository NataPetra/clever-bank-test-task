package by.nata.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Config {

    private Integer interestToBeCharged;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{");
        sb.append("interestToBeCharged=").append(interestToBeCharged);
        sb.append('}');
        return sb.toString();
    }
}
