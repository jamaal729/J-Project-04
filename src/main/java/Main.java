
import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.CommentDao;
import com.teamtreehouse.blog.dao.SimpleBlogDao;
import com.teamtreehouse.blog.dao.SimpleCommentDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import spark.ModelAndView;

import spark.template.handlebars.HandlebarsTemplateEngine;
import com.github.slugify.Slugify;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final BlogDao blogDao = new SimpleBlogDao();
    private static final CommentDao commentDao = new SimpleCommentDao();
    private static final String currentPath = "";

    private static String sampleTitle;
    private static String sampleBody;

    private static final HandlebarsTemplateEngine hbtEngine = new HandlebarsTemplateEngine();

    public static void main(String[] args) {

        generateSampleEntries();
        staticFileLocation("/public");

        // Set cookie for password
        before((req, res) -> {
            if (req.cookie("password") != null) {
                req.attribute("password", req.cookie("password"));
            }
        });

        // Login required to add blog entry
        before("/new", (req, res) -> {
            if (req.attribute("password") == null) {
                req.session().attribute(currentPath, req.uri());
                res.redirect("/password");
                System.out.println(req.attributes());
                halt();
            }
        });

        // Login required to edit blog entry
        before("/edit/:slug", (req, res) -> {
            if (req.attribute("password") == null) {
                req.session().attribute(currentPath, req.uri());
                res.redirect("/password");
                halt();
            }
        });

        // Display home page
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entries", blogDao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, hbtEngine);

        // Display new blog page
        get("/new", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            return new ModelAndView(model, "new.hbs");
        }, hbtEngine);

        // Display password page
        get("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            return new ModelAndView(model, "password.hbs");
        }, hbtEngine);

        // Verify password
        post("/password", (req, res) -> {
            if (!req.queryParams("password").equals("admin")) {
                res.redirect("/password");
            } else {
                String path = (String) req.session().attribute(currentPath);
                req.session().removeAttribute(currentPath);
                res.cookie("password", "admin");
                res.redirect(path);
            }
            return null;
        });

        // Display the blog edit page
        get("/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            BlogEntry blogEntry = blogDao.findEntryBySlug(req.params(":slug"));
            model.put("entry", blogEntry);
            return new ModelAndView(model, "edit.hbs");
        }, hbtEngine);

        // Add blog post to entries
        post("/", (req, res) -> {
            String title = req.queryParams("title");
            String body = req.queryParams("entry");
            if (!title.isEmpty()) {
                BlogEntry blogEntry = new BlogEntry(title, body);
                blogDao.addEntry(blogEntry);
                res.redirect("/");
            } else {
                res.redirect("/new");
            }
            return null;
        });

        // Display details page for blog post
        get("/entry/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            BlogEntry blogEntry = blogDao.findEntryBySlug(req.params(":slug"));
            model.put("entry", blogEntry);
            return new ModelAndView(model, "detail.hbs");
        }, hbtEngine);

        // New stuff to replace the fields for each post
        post("/entry/:slug", (req, res) -> {
            Slugify slg = new Slugify();
            String newTitle = req.queryParams("title");
            String newBody = req.queryParams("entry");
            String newSlug = slg.slugify(newTitle);

            String slug = req.params("slug");
            BlogEntry blogEntry = blogDao.findEntryBySlug(slug);

            // The post must have a title
            if (!newTitle.isEmpty()) {
                Map<String, Object> model = new HashMap<>();
                blogDao.editEntry(blogEntry, newTitle, newBody, newSlug);
                model.put("entry", blogEntry);
                res.redirect("/entry/" + newSlug);
                return new ModelAndView(model, "detail.hbs");
            } else {
                res.redirect("/entry/" + slug);
            }
            return null;
        }, hbtEngine);

        // Adding a comment to a blog entry
        post("/entry/:slug/comment", (req, res) -> {
            String slug = req.params("slug");
            commentDao.addComment(blogDao.findEntryBySlug(slug),
                    new Comment(req.queryParams("name"), req.queryParams("comment")));
            res.redirect("/entry/" + slug);
            BlogEntry blogEntry = blogDao.findEntryBySlug(slug);
            return null;
        });
    }

    private static void generateSampleEntries() {

        sampleTitle = "What is a blog?";
        sampleBody = "Blog is another word for weblog. A weblog is a website that is like a diary or journal. Most people can create a blog and then write on that blog. Bloggers (a word for people who write on blogs) often write about their opinions and thoughts.";
        blogDao.addEntry(new BlogEntry(sampleTitle, sampleBody));

        sampleTitle = "Writing a blog post";
        sampleBody = "When a person writes on a blog, what they write is in the form of a post, which is a single piece of writing on the blog. Posts often include links to other websites.";
        blogDao.addEntry(new BlogEntry(sampleTitle, sampleBody));

        sampleTitle = "Types of blogs";
        sampleBody = "Blogs can have one or more writers. If they have more than one writer, they are often called community blogs, team blogs, or group blogs.";
        blogDao.addEntry(new BlogEntry(sampleTitle, sampleBody));
    }
}

