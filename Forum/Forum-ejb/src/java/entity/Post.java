package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String title;
    
    private String text;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ForumUser forumUser;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ForumThread thread;
    
//    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date postDateTime;

    public Post() {
    }

    public Post(String title, String text) {
        this.title = title;
        this.text = text;
        //this.postDateTime = new Date();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Post[ id=" + getId() + " ]";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ForumUser getUser() {
        return getForumUser();
    }

    public void setUser(ForumUser forumUser) {
        this.setForumUser(forumUser);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Date getPostDateTime() {
//        return postDateTime;
//    }
//
//    public void setPostDateTime(Date postDateTime) {
//        this.postDateTime = postDateTime;
//    }

    public ForumUser getForumUser() {
        return forumUser;
    }

    public void setForumUser(ForumUser forumUser) {
        this.forumUser = forumUser;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }   
}
