FROM node:6

# �� ���͸� ����
WORKDIR D:/TLC\tlc_service/tlc_service/web/web/front_dev/webapp/kr/co/tlccpd/webapp/node_modules/TLC

# �� ������ ��ġ
COPY package*.json ./
RUN npm install

# �� �ҽ� �߰�
COPY . .

EXPOSE 3000
CMD [ "npm", "start" ]