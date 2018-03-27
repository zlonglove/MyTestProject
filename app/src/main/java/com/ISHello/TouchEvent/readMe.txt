Android系统中的每个View的子类都具有下面三个和TouchEvent处理密切相关的方法：
1）public boolean dispatchTouchEvent(MotionEvent ev)  这个方法用来分发TouchEvent
2）public boolean onInterceptTouchEvent(MotionEvent ev) 这个方法用来拦截TouchEvent
3）public boolean onTouchEvent(MotionEvent ev) 这个方法用来处理TouchEvent

当 TouchEvent发生时，首先Activity将TouchEvent事件通过dispatchTouchEvent方法传递给ViewGroup

注：以下所说的子view和父view均是包含关系，不是继承关系
1，ViewGroup通过dispatchTouchEvent方法传递给interceptTouchEvent：
    （1）如果返回true ，则交给这个ViewGroup的onTouchEvent处理。
    （2）如果返回false，则交给子View的 dispatchTouchEvent方法处理。
2，事件传递到子view 的 dispatchTouchEvent方法中，通过方法传递到当前View的onTouchEvent方法中：

    （1）如果返回true，那么这个事件就会止于该view。
    （2）如果返回 false ，那么这个事件会从这个子view 往上传递，而且都是传递到父View的onTouchEvent 来接收。

    （3）如果传递到ViewGroup的 onTouchEvent 也返回 false 的话，则继续传递到Activity的onTouchEvent中，
         如果还是false,则这个事件就会“消失“；事件向上传递到中间的任何onTouchEvent方法中，如果返回true，则事件被消费掉，不会再传递。




总结：
1.事件传递的两种方式：
隧道方式：从根元素依次往下传递直到最内层子元素或在中间某一元素中由于某一条件停止传递。
冒泡方式：从最内层子元素依次往外传递直到根元素或在中间某一元素中由于某一条件停止传递。
2.android对Touch Event的分发逻辑是View从上层分发到下层（dispatchTouchEvent函数）类似于隧道方式，然后下层优先开始处理Event
（先mOnTouchListener，再onTouchEvent）并向上返回处理情况（boolean值），若返回true，则上层不再处理，类似于冒泡方式。

3.touch事件分析：
事件分发：public boolean dispatchTouchEvent(MotionEvent ev)
  Touch 事件发生时 Activity 的 dispatchTouchEvent(MotionEvent ev) 方法会以隧道方式
  （从根元素依次往下传递直到最内层子元素或在中间某一元素中由于某一条件停止传递）将事件传递给最外层 View 的
  dispatchTouchEvent(MotionEvent ev) 方法，并由该 View 的 dispatchTouchEvent(MotionEvent ev) 方法对事件进行分发。
  dispatchTouchEvent 的事件分发逻辑如下：
如果 return true，事件会分发给当前 View 并由 dispatchTouchEvent 方法进行消费，同时事件会停止向下传递；
 如果 return false，事件分发分为两种情况：
如果当前 View 获取的事件直接来自 Activity，则会将事件返回给 Activity 的 onTouchEvent 进行消费；
如果当前 View 获取的事件来自外层父控件，则会将事件返回给父 View 的  onTouchEvent 进行消费。
  如果返回系统默认的 super.dispatchTouchEvent(ev)，事件会自动的分发给当前 View 的 onInterceptTouchEvent 方法。

事件拦截：public boolean onInterceptTouchEvent(MotionEvent ev)
  在外层 View 的 dispatchTouchEvent(MotionEvent ev) 方法返回系统默认的 super.dispatchTouchEvent(ev) 情况下，
  事件会自动的分发给当前 View 的 onInterceptTouchEvent 方法。onInterceptTouchEvent 的事件拦截逻辑如下：
如果 onInterceptTouchEvent 返回 true，则表示将事件进行拦截，并将拦截到的事件交由当前 View 的 onTouchEvent 进行处理；
如果 onInterceptTouchEvent 返回 false，则表示将事件放行，当前 View 上的事件会被传递到子 View 上，再由子 View 的 dispatchTouchEvent
来开始这个事件的分发；
如果 onInterceptTouchEvent 返回 super.onInterceptTouchEvent(ev)，事件默认会被拦截，并将拦截到的事件交由当前 View 的 onTouchEvent
进行处理。
事件响应：public boolean onTouchEvent(MotionEvent ev)
  在 dispatchTouchEvent 返回 super.dispatchTouchEvent(ev) 并且 onInterceptTouchEvent 返回 true 或返回
  super.onInterceptTouchEvent(ev) 的情况下 onTouchEvent 会被调用。onTouchEvent 的事件响应逻辑如下：
如果事件传递到当前 View 的 onTouchEvent 方法，而该方法返回了 false，那么这个事件会从当前 View 向上传递，并且都是由上层 View 的
onTouchEvent 来接收，如果传递到上面的 onTouchEvent 也返回 false，这个事件就会“消失”，而且接收不到下一次事件。
如果返回了 true 则会接收并消费该事件。
如果返回 super.onTouchEvent(ev) 默认处理事件的逻辑和返回 false 时相同。