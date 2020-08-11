package com.example.interview.event;

import com.example.interview.model.Article;
import org.springframework.context.ApplicationEvent;

public class ArticleEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ArticleEvent(Article source) {
        super(source);
    }

    @Override
    public Article getSource() {
        return (Article) super.getSource();
    }
}
