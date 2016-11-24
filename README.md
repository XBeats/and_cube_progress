# and_cube_progress
正方体样式的loadingView

在[dribbble上看到的一个gif效果](https://dribbble.com/shots/1927432-PCPP-Loading-Boxes-Update)，觉得还不错。主要用动画控制，思路还是比较简单的。
![image](http://ww2.sinaimg.cn/mw690/88120a10gw1f9wp5p7kn9g20b408cwkn.gif) 

## 思路   


 1. 先要确定一个正方体如何画，由图可以看出来一个正方体有三部分组成，上部分（色值#FDFDE3）、左下部分（色值#EEDC70）和右下部分（色值#FAECA4）  
  ![image](https://raw.githubusercontent.com/XBeats/and_cube_progress/master/screenshot/one_cube.png)  
 2. 这三个部分其实都是一个四边相等的平行四边形（菱形），这里用path来确定每个的四边形，然后通过Canvas的变换，分配各个四边形到各自的位置，就能行成一个正方体的样式
 3. 接下来就是要确定每个正方体的移动轨迹，就是一个选择参考点的过程，如图中选择每个正方体中的A点作为参考点，控制A点的移动，就能达到整体的移动效果  
 ![image](https://raw.githubusercontent.com/XBeats/and_cube_progress/master/screenshot/one_cube_a.png)
 4. 阴影部分其实和上部分一致，只是位置移动到了正方体的下面，处理起来还是比较简单的
 5. 将四个立方体按照从右往左，从上往下，依次编号（1，2，3，4） 
 6. 动画分成两部分，一是上下平移，二是左右平移；并用变量`mIsFirstPart`来区分（解决多层覆盖绘制过程）
 7. 实际上整个过程只用了一个动画来完成的，编号1，4的立方体只移动了动画的上半部分，所以需要控制其最大/最小值。编号2，3的立方体移动了动画的完整过程。
 
>注：在立方体移动过程中，需要注意四个立方体绘制的先后顺序，不然会出现重叠情况，难以产生立体感。


## 效果图  
![image](https://raw.githubusercontent.com/XBeats/and_cube_progress/master/screenshot/cube_progress.gif)

