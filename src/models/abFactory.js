/**
 * 使用抽象工厂模式管理消息类
 * 三类： 点赞，评论，@
 *  */ 

// 抽象工厂
class MessageFactory {
    constructor() {
        throw new Error('抽象工厂方法，需要子类实现')
    }
}

// 抽象消息
class Message{
    constructor(data) {
        this.id = data.id
        this.type = data.type
        this.content = data.content
        this.createTime = data.createTime
        this.sender = data.sender // 发送人
        this.receiver = data.receiver
        this.url = data.url // url来源
    }
    getMessage(){
        return new Message(data);
    }
    // 点击后跳转来源
    click(){
        window.open(this.url);
        
        // TODO跳转到页面锚点滚动+id，使用重新渲染
    }
}

// 具体点赞工厂
class LikeMessageFactory extends MessageFactory {

}

// 具体评论评论
class CommentMessageFactory extends MessageFactory {

}

// 具体@评论
class CommentMessageFactory extends MessageFactory {
    createMessage(){
        return new CommentMessage(data);
    }
}

// 