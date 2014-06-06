spring-cache-memcached
======================

Integrating memcached with spring-cache


##Configuration
An example configuration is below.

    <bean id="memcachedClient" class="net.spy.memcached.spring.MemcachedClientFactoryBean">
        <property name="servers" value="127.0.0.1:11211"/>
        <property name="protocol" value="BINARY"/>
        <property name="opTimeout" value="1000"/>
        <property name="daemon" value="true"/>
    </bean>

    <bean id="defaultCache" class="org.springframework.cache.memcached.MemcachedCache">
        <property name="name" value="default"/>
        <property name="client" ref="memcachedClient"/>
    </bean>

    <bean id="cacheManager" class="org.springframework.cache.memcached.MemcachedCacheManager" destroy-method="shutdown">
        <property name="client" ref="memcachedClient"/>
        <property name="caches">
            <set>
                <ref bean="defaultCache"/>
            </set>
        </property>
    </bean>

