#!/bin/zsh

rm *.class
padding=$1
threadsNum=$2
if [ -z "$padding" ]
then
    echo '请选择对应的内存填充方式：不填充选0，手动填充选1，自动填充选2'
    read -r padding
fi
if [ -z "$threadsNum" ]
then
    echo '请输入对应的并发线程数量'
    read -r threadsNum
fi
case $padding in
    0)
      echo "start no padding with $threadsNum threads"
      javac -nowarn NoPadding.java
      time java NoPadding "$threadsNum"
      ;;
    1)
      echo "start manual padding with $threadsNum threads"
      javac -nowarn -XDenableSunApiLintControl ManualPadding.java
      time java ManualPadding "$threadsNum"
      ;;
    2)
      echo "start auto padding with $threadsNum threads"
      javac -nowarn -XDenableSunApiLintControl AutoPadding.java
      time java -XX:-RestrictContended AutoPadding "$threadsNum"
      ;;
    *)  echo'请选择正确的内存填充方式（0、1、2）'
      ;;
esac