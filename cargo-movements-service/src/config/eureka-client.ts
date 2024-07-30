import { Eureka } from 'eureka-js-client';

const HOSTNAME = 'cargo-movements-service';
const IP_ADDR = '172.0.0.1';
const PORT_ENABLED = 'true';
const EUREKA_HOST = 'eurekaserver';
const EUREKA_PORT = 8070;
const EUREKA_SERVICE_PATH = '/eureka/apps/';
const isPortEnabled = PORT_ENABLED === 'true';


export function registerWithEureka(appName: string, port: number): void {
    const instanceId = `${IP_ADDR}:${appName}:${port}`;
    const eurekaClient = new Eureka({
        instance: {
            instanceId: instanceId,
            app: appName,
            hostName: HOSTNAME,
            ipAddr: IP_ADDR,
            port: {
                $ : port,
                '@enabled': isPortEnabled,
            },
            vipAddress: appName,
            dataCenterInfo: {
                '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                name: 'MyOwn',
            },
        },
        eureka: {
            host: EUREKA_HOST,
            port: EUREKA_PORT,
            servicePath: EUREKA_SERVICE_PATH,
            maxRetries: 5,
            requestRetryDelay: 2000,
        },
    });

    eurekaClient.start((error: Error) => {
        if (error) {
            console.error('Error registering with Eureka:', error.message);
            process.exit(1);
        } else {
            console.log(`${appName} service registered with Eureka`);
        }
    });

    process.on('SIGINT', () => {
        eurekaClient.stop(err => {
            if (err) {
                console.error('Error deregistering from Eureka:', err.message);
            } else {
                console.log('Deregistered from Eureka successfully');
            }
            process.exit(err ? 1 : 0);
        });
    });
}
