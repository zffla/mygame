# Puzzle-Game
简单的拼图游戏，可选难度2x2、3x3、4x4，支持拍照和从本地图库选择图片。
## 界面展示
### 选择图片
<img src="https://github.com/zffla/mygame/blob/master/index.jpg" width=30%/>  <img src="https://github.com/zffla/mygame/blob/master/select-photo.jpg" width=30%/>

### 游戏界面
<img src="https://github.com/zffla/mygame/blob/master/game-2.jpg" width=30%/> <img src="https://github.com/zffla/mygame/blob/master/game-3.jpg" width=30%/> <img src="https://github.com/zffla/mygame/blob/master/game-4.jpg" width=30%/>

<img src="https://github.com/zffla/mygame/blob/master/succeed.jpg" width=30%/>

## 具体实现
### 图片切分
拼图前首先需要将图片切分(ImagesUtil.createInitBitmaps(type,pic,context))，步骤如下：
1. 根据type类型，即2x2、3x3或4x4确定图片需要切分的块数；
2. 确定每块图片大小；
3. 将切分后的每块图片存入实体类ItemBean(itemId,bitmapId,bitmap)中；
4. 将切分后的图片列表存入bitmap列表中，并去除最后一块图片，将其替换为空白图片。
5. 拼图预处理完成。

关键代码：
```java
//获取bitmap宽高
int itemWidth=pic.getWidth()/type;
int itemHeight=pic.getHeight()/type;
//生成小图，i、j代表行列索引
Bitmap bitmap=Bitmap.createBitmap(pic,j*itemWidth,i*itemHeight,itemWidth,itemHeight);
bitmapList.add(bitmap);
ItemBean itemBean=new ItemBean(i*type+j+1,i*type+j+1,bitmap);
```

### 游戏界面
包含两个关键部分：数据上打乱图片顺序，界面上点击交换图片位置
#### 打乱图片顺序
1. 随机获取index，并和空白图交换bitmapId、bitmap；
2. 计算倒置和，根据倒置和判断是否有解，若有解，转3，否则转1重新打乱顺序；
3. 图片打乱顺序有解，处理完成。

注意：1.始终保存空白图片位置，用于交换位置以及拼图成功时补充成完整图片；2.拼图中有些顺序无解，需要通过倒置和判断。

#### 交换图片位置
1. 判断当前点击的图片是否可以移动，若在相同行需要与空白图位置差1，不同行则差type；
2. 在list中交换图片，并通知adapter数据更新，调用mAdapter.notifyDataSetChanged()更新界面。

### 其他
1.记录移动次数和耗时；
2.点击原图button，显示完整图片以作提示；
3.点击重置，重新生成拼图界面。
