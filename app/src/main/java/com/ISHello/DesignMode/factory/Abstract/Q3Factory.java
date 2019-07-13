package com.ISHello.DesignMode.factory.Abstract;

import com.ISHello.DesignMode.factory.Abstract.Brake.IBrake;
import com.ISHello.DesignMode.factory.Abstract.Brake.NomalBrake;
import com.ISHello.DesignMode.factory.Abstract.Engine.DomesticEngine;
import com.ISHello.DesignMode.factory.Abstract.Engine.IEngine;
import com.ISHello.DesignMode.factory.Abstract.Tire.ITire;
import com.ISHello.DesignMode.factory.Abstract.Tire.NomalTire;

public class Q3Factory implements CardFactory {
    @Override
    public ITire createTire() {
        return new NomalTire();
    }

    @Override
    public IEngine createEngine() {
        return new DomesticEngine();
    }

    @Override
    public IBrake createBrake() {
        return new NomalBrake();
    }
}
