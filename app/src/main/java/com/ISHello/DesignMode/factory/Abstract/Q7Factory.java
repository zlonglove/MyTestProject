package com.ISHello.DesignMode.factory.Abstract;

import com.ISHello.DesignMode.factory.Abstract.Brake.IBrake;
import com.ISHello.DesignMode.factory.Abstract.Brake.SeniorBrake;
import com.ISHello.DesignMode.factory.Abstract.Engine.IEngine;
import com.ISHello.DesignMode.factory.Abstract.Engine.ImportEngine;
import com.ISHello.DesignMode.factory.Abstract.Tire.ITire;
import com.ISHello.DesignMode.factory.Abstract.Tire.SUVTire;

public class Q7Factory implements CardFactory {
    @Override
    public ITire createTire() {
        return new SUVTire();
    }

    @Override
    public IEngine createEngine() {
        return new ImportEngine();
    }

    @Override
    public IBrake createBrake() {
        return new SeniorBrake();
    }
}
