# PinoGL
PinoGLはPinoの内部で使用される予定の描画ライブラリです。  

## Why PinoGL?  
現在、Pinoの内部では主にJava2Dを使用してレンダリングを行っています。しかし、Java2Dを使用した場合次のような課題がありました。  
- BufferedImageを使用した場合、基本的にソフトウェアループを使用して計算が行われる
- VolatileImageを使用した場合、標準APIを使用した固定パイプラインしか使用できない。
- AlphaCompositeでは合成規則が足りない

Pinoの制作において、これらの課題の解決は必須でした。

Java2Dを使用しない場合の選択肢としては、JOGLなどもありますが、DirectXを使用したい場合などに拡張が難しくなると考えられるので、
直接依存するのはNGでした。

そこで、Java2D, Skia, Prismなどを参考に新たなレンダリングエンジンを作成しています。

また、Java2Dには次のような課題もあります。  
- ImageIOでは出力オプションの指定が難しい
- レンダリングエンジンを使用したいだけなのに、java.desktopという巨大なモジュールに依存する

これらの課題はPinoの制作においてはあまり大きな問題とはなりませんでしたが、なるべく解決したいと考えています。

## Design
PinoGLはレンダリングのための情報をカプセル化するインターフェースで処理の実体はBackendです。

このようにAPIと実装を分離することで、Backendを切り替えるだけで簡単にOpenGLやDirectXなど処理を切り替えられるようになっています。

Backendの取得はServiceLoaderを介して行います。下の図は、Backendを取得する方法を示したものです。

```java
var descs = ServiceLoader.load(PlatformDescriptor.class);
var desc = /* your favorite platform's descriptor */
var platform = desc.getProvider().get();
```

このようにしてBackendを取得した後は、ResourceFactoryやPlatformを使用してTexture等を作成しレンダリングを行います。