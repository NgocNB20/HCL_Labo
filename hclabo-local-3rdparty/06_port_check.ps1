Get-NetTCPConnection -LocalPort 80,443,5432,5672,8080,8081,8180,8280,8380,8480,8580,8680,8780,8880,8980,58080,9411,15672,18080,24224,27017,28081,60001,60002,60003 | fl

echo ""

echo "��L�R�}���h���ʂ��A���ׂẴ|�[�g�ɂ��āA�ԕ�����"
Write-Host "Get-NetTCPConnection : �v���p�e�B 'LocalPort' �� 'xx' �� MSFT_NetTCPConnection �I�u�W�F�N�g��������܂���B"  -ForegroundColor Red
echo "�ł��邱�Ƃ��m�F���Ă��������B"

echo ""

echo "1���ł��������ő҂��󂯃|�[�g���������ꍇ�́A�Y������ OwningProcess �̃v���Z�XID�̑��A�v���Ƌ������Ă��܂��B"
echo "���̃A�v�����~���Ă���A�\�z�菇��i�߂Ă��������B"
echo "���̂܂ܐi�߂�ƁA�K�v�ȃA�v���̃C���X�g�[����Docker�R���e�i�̋N���Ɏ��s���A�菇�ʂ�ɐi�݂܂���B"

echo ""

