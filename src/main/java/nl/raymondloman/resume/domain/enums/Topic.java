package nl.raymondloman.resume.domain.enums;

public enum Topic {
    Android(3),
    Java(5),
    IntelliJ(5),
    Compilers(3),
    Grails(0),
    Groovy(0),
    Tomcat(5),
    Eclipse(5),
    ELK(3),
    jQuery(5),
    Spring(4),
    JVM(0),
    REST(5),
    SOAP(4),
    Spring_Boot(5),
    Angular(5),
    JHipster(4),
    JAXRS(5),
    JAXWS(5),
    Java_EE(5),
    JSON(5),
    Jackson(5),
    JSF(2),
    Git(5),
    Jenkins(5),
    Selenium(0),
    Cucumber(5),
    GWT(0),
    Linux(5),
    Windows(4),
    MySQL(5),
    Hibernate(5),
    JavaScript(5),
    TypeScript(4),
    XML(5),
    ANTLR(2),
    CUP(0),
    Beaver(5),
    JFlex(4),
    ASP(0),
    PHP(0),
    Progress_4GL(0),
    Novell_Netware(0),
    Cisco_iOS(0),
    C(4),
    Assembly(4),
    Clipper(0),
    DB4(0),
    DataStar(0),
    PC_FOCUS(0),
    Jersey(3),
    JPA(5),
    Continuous_Integration(5),
    Continuous_Delivery(5),
    MicroServices(5),
    DevOps(4),
    Maven(5),
    Git_Flow(5),
    Docker(4),
    Scrum(5),
    CPP(4),
    Apache(5),
    NGinx(4),
    Sonar(5),
    Artifactory(4),
    Nexus(2),
    JUnit(5),
    Mockito(5),
    Weblogic(0),
    Ruby(0),
    Rails(0),
    RUP(0),
    UML(4),
    JMS(3),
    PostgreSQL(3),
    JBoss(3),
    JMeter(2),
    Gradle(2),
    HTML5(5),
    SSL(4),
    CSS3(2), 
    Spring_MVC(2), Spring_Security(5),
    CPP_Standard_Library(4), CPP_Standard_Template_Library(3), 
    Bootstrap(2),
    Security(3), OWASP(3), WebGoat(3), NodeJS(3);

    private int expectedStars;

    private Topic(int expectedStars) {
        this.expectedStars = expectedStars;
    }

    public int getExpectedStars() {
        return expectedStars;
    }

    //@formatter:off
    @Override
    public String toString() {

        switch (this) {
            case JAXRS:
                return "JAX-RS";
            case JAXWS:
                return "JAX-WS";
            case CPP:
                return "C{plus}{plus}";
            case ELK:
                return "ELK (ElasticSearch, LogStash, Kibana)";
            case CPP_Standard_Library:
                return "C{plus}{plus} Standard Library";
            case CPP_Standard_Template_Library:
                return "C{plus}{plus} Standard Template Library";
            case SSL:
                return "SSL/TLS";
            default:
                return this.name().replaceAll("_", " ");
        }
    }
//@formatter:on
}