package com.ISHello.DesignMode.factory.Abstract;

import com.ISHello.DesignMode.factory.Abstract.Brake.IBrake;
import com.ISHello.DesignMode.factory.Abstract.Engine.IEngine;
import com.ISHello.DesignMode.factory.Abstract.Tire.ITire;

public interface CardFactory {
    /**
     * 生产轮胎
     *
     * @return 返回轮胎对象
     */
    ITire createTire();

    /**
     * 生产发动机
     *
     * @return 返回发动机对象
     */
    IEngine createEngine();

    /**
     * 生产制动系统
     *
     * @return 返回制动系统
     */
    IBrake createBrake();

}
