package haddle.stellarorigins.components;

import dev.onyxstudios.cca.api.v3.component.Component;

// Your own component interface. You still need to register it!
public interface StarpowerComponent extends Component {
    double getValue();
    void change(double amount);
    double starpower = 0;
    double maximum_starpower = 0;
}

