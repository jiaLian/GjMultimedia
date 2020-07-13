[![Download](https://api.bintray.com/packages/jialian/goodJia/multimedia/images/download.svg) ](https://bintray.com/jialian/goodJia/multimedia/_latestVersion)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Multiple Media Component
The library can play photo, video, youtube and custom fragment.
It is fragment player.

## Demo

<img src="/demo/autoplay.gif" width="280px"/>       <img src="/demo/list.gif" width="280px"/>

# How to Use

**1 . root/build.gradle**
````gradle
allprojects {
    repositories {
        ...
        maven { url "https://dl.bintray.com/jialian/goodJia" }
    }
}
````

**2. app/build.gradle**
````gradle
implementation 'com.goodjia:multimedia:'0.1.4.1'      //this is use of android.support
or
implementation 'com.goodjia:multimedia:'{_latestVersion}'      //this is use of androidx
````

**3. Task, create task list**
````kotlin
tasks = arrayListOf(
        Task(
            Task.ACTION_CUSTOM, playtime = 3,
            className = CustomTaskFragment::class.java.name,
            bundle = CustomTaskFragment.bundle("Custom 1")
        ),
        Task(
            Task.ACTION_IMAGE,
            "https://media.wired.com/photos/598e35994ab8482c0d6946e0/master/w_1164,c_limit/phonepicutres-TA.jpg",
            playtime = 3
        ),
        Task(
            Task.ACTION_CUSTOM,
            playtime = 10,
            className = CustomTaskFragment::class.java.name,
            bundle = CustomTaskFragment.bundle("Custom 2")
        ),
        Task(
            Task.ACTION_VIDEO,
            "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_10mb.mp4"
        ),
        Task(Task.ACTION_YOUTUBE, "https://youtu.be/nSbCMxSaBaw")
        )
````
**4. Start MultimediaPlayerFragment**
````kotlin
//Video source size: Wrap content (origin center) 
/*val multimediaPlayerFragment = 
               MultimediaPlayerFragment.newInstance(tasks, ViewGroup.LayoutParams.WRAP_CONTENT)*/
//Video source size: default fit center
val multimediaPlayerFragment = 
               MultimediaPlayerFragment.newInstance(tasks)
                //animationCallback: switch page animation
                multimediaPlayerFragment?.animationCallback = object : MediaFragment.AnimationCallback {
                    override fun animation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//                        return if (enter) CubeAnimation.create(CubeAnimation.RIGHT, enter, DURATION).fading(0.3f, 1.0f)
//                        else MoveAnimation.create(MoveAnimation.RIGHT, enter, DURATION).fading(1.0f, 0.3f)

                            //use transaction animation object 
                            val ANIMATIONS = listOf(
                                       TransitionAnimation(
                                           TransitionAnimation.AnimationType.CUBE.name,
                                           TransitionAnimation.Direction.DOWN.name,
                                           500
                                       ),
                                       TransitionAnimation(
                                           TransitionAnimation.AnimationType.MOVE.name,
                                           TransitionAnimation.Direction.UP.name,
                                           800
                                       ),
                                       TransitionAnimation(
                                           TransitionAnimation.AnimationType.FLIP.name,
                                           TransitionAnimation.Direction.LEFT.name,
                                           300
                                       ),
                                       TransitionAnimation(
                                           TransitionAnimation.AnimationType.SIDES.name,
                                           TransitionAnimation.Direction.RIGHT.name,
                                           1200
                                       ),
                                       TransitionAnimation(
                                           TransitionAnimation.AnimationType.PUSHPULL.name,
                                           TransitionAnimation.Direction.RIGHT.name,
                                           600
                                       ),
                                       TransitionAnimation(TransitionAnimation.AnimationType.NONE.name)
                                   )                           
                           
                            return ANIMATIONS[kotlin.random.Random.nextInt(ANIMATIONS.size)].getAnimation(
                                enter
                            ).apply {
                                Log.d(PlayerListFragment.TAG, "animation $this")
                            }
                    }
                }
                // Player listener
                multimediaPlayerFragment?.playerListener = object : MultimediaPlayerFragment.PlayerListener {
                    override fun onLoopCompletion() {
                        Log.d(TAG, "onLoopCompletion")
                    }

                    override fun onPrepared(playerFragment: MultimediaPlayerFragment) {
                        val volume = Random().nextInt(100)
                        Log.d(TAG, "onPrepared $volume")
                        playerFragment.setVolume(volume)
                    }

                    override fun onChange(position: Int, task: Task) {
                        Log.d(TAG, "onChange $position, task $task")
                    }

                    override fun onError(position: Int, task: Task, action: Int, message: String?) {
                        Log.d(TAG, "onError $position, task $task, error $message")
                    }
                }
                start(multimediaPlayerFragment)
````
**5. Show MultimediaPlayerPresentation (Secondary Screen)**
````kotlin
context?.displayManager?.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
            ?.get(0)?.let {
                multimediaPlayerPresentation = MultimediaPlayerPresentation.newInstance(
                    context!!,
                    it, presentationTasks
                )
            }

        multimediaPlayerPresentation?.animationCallback =
            object : MediaFragment.AnimationCallback {
                override fun animation(
                    transit: Int,
                    enter: Boolean,
                    nextAnim: Int
                ): Animation? {
                    return ANIMATIONS[Random.nextInt(ANIMATIONS.size)].apply {
                        Log.d(TAG, "presentation animation $this")
                    }.getAnimation(enter)
                }
            }

        multimediaPlayerPresentation?.playerListener =
            object : MultimediaPlayerFragment.PlayerListener {
                override fun onLoopCompletion() {
                    Log.d(TAG, "presentation onLoopCompletion")
                }

                override fun onPrepared(playerFragment: MultimediaPlayerFragment) {
                    playerFragment.setVolume(0)
                }

                override fun onChange(position: Int, task: Task) {
                    Log.d(TAG, "presentation onChange $position, task $task")
                }

                override fun onError(
                    position: Int,
                    task: Task?,
                    action: Int,
                    message: String?
                ) {
                    Log.d(TAG, "presentation onError $position, task $task, error $message")
                }
            }
        multimediaPlayerPresentation?.show(fragmentManager!!, "secondary")
````

**6. Change Play other task**
````kotlin
val otherTask=Task()
...
multimediaPlayerFragment?.play(otherTask)
````

**7. Change play the position of task list**
````kotlin
multimediaPlayerFragment?.play(position)
````
**8. Check can support image/video local file with file name**
````kotlin
//return true is video format with file name (can support mp4)
isVideoFile(filename)
//return true is image format with file name (can support jpg and png)
isImageFile(filename)
````

**.. Start Youtube Fragment**
````kotlin
start(YoutubeFragment.newInstance("https://www.youtube.com/watch?v=IduYAx4ptNU"))
````

## LICENSE
````
Copyright 2018 Jia

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
````
